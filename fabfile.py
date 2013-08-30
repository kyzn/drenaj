from __future__ import with_statement
from fabric.api import local, settings, run, abort, env, cd, prefix
from fabric.contrib.console import confirm

#env.forward_agent = True
#env.gateway = 'onur@lsn'

hostname = 'voltran'
env.hosts = ['direnaj@%s' % hostname]

env.use_ssh_config = True

code_dir = '/home/direnaj/direnaj/envs/staging'
repo_dir = '/home/direnaj/direnaj/repo'
deployment_repo_remote_name = 'deployment_repo_%s' % hostname

def test():
    with settings(warn_only=True):
        result = local('py.test -q', capture=True)
    if result.failed and not confirm("Tests failed. Continue anyway?"):
        abort("Aborting at user request.")

def setup_deployment_repo():

    ensure_apt_package("git")

    with settings(warn_only=True):
        if run("test -d %s" % repo_dir).failed:
            run("git init --bare %s" % repo_dir)
    with settings(warn_only=True):
        result = local("git remote | grep ^%s$" % deployment_repo_remote_name)
        if result.failed:
            local("git remote add %s direnaj@%s:%s" % (deployment_repo_remote_name, hostname, repo_dir))

def push():
    local("git push %s master" % deployment_repo_remote_name)

def prepare_deploy():
    #test()
    setup_deployment_repo()
    push()

def deploy():
    with settings(warn_only=True):
        if run("test -d %s" % code_dir).failed:
            run("git clone %s %s" % (repo_dir, code_dir))
    with cd(code_dir):
        run("git pull")

def ensure_apt_package(package_name):
    with settings(warn_only=True):
        result = run("dpkg-query -s %s" % package_name)
    if result.failed:
        run('DEBIAN_FRONTEND=noninteractive sudo apt-get -q --yes -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" install %s' % package_name)
        # run("sudo apt-get install python-pip")

def setup_environment():

    # ensure mongodb-10gen is installed
    with settings(warn_only=True):
        result = run("sudo apt-key export 7F0CEB10")
    if "-----BEGIN PGP PUBLIC KEY BLOCK-----" not in result:
        run("sudo apt-key adv --keyserver keyserver.ubuntu.com --recv 7F0CEB10")
    with settings(warn_only=True):
        result = run("test -f /etc/apt/sources.list.d/mongodb.list")
    if result.failed:
        run("su -c 'echo deb http://downloads-distro.mongodb.org/repo/debian-sysvinit dist 10gen > /etc/apt/sources.list.d/mongodb.list'")
        run("sudo apt-get update")
    ensure_apt_package("mongodb-10gen")

    # ensure pip is installed.
    ensure_apt_package("python-pip")
    # ensure python development headers and other files are installed.
    ensure_apt_package("python-dev")
    # ensure virtualenv is installed
    run("sudo pip install virtualenv")
    # ensure virtualenvwrapper is installed
    run("sudo pip install virtualenvwrapper")

    virtualenv_dir = '/home/direnaj/.virtualenvs/direnaj/'
    with settings(warn_only=True):
        result = run("test -d %s" % virtualenv_dir)
    with prefix("source /usr/local/bin/virtualenvwrapper.sh"):
        if result.failed:
            run("mkvirtualenv direnaj")

    # ensure libcurl-dev is installed. it might be libcurl4-gnutls-dev
    ensure_apt_package("libcurl4-gnutls-dev")

    with prefix("source /usr/local/bin/virtualenvwrapper.sh"),\
         prefix("workon direnaj"):
        with cd(code_dir):
            run("pip install -r env/env_requirements.txt")
            run("python configure.py host-configs/config-%s.yaml direnaj/config.py" % hostname)

def run_server():
    # ensure mongodb is running
    # bla bla... maybe use supervisord is it possible?
    # start the main direnaj process
    with prefix("source /usr/local/bin/virtualenvwrapper.sh"),\
         prefix("workon direnaj"):
        with cd(code_dir):
            # make sure there is a logs dir.
            run("mkdir -p logs")
            with settings(warn_only=True):
                result = run("test -S /tmp/supervisor.sock")
            if result.failed:
                with prefix("supervisord -c supervisord.conf"):
                    run("supervisorctl -s unix:///tmp/supervisor.sock restart direnaj")
            else:
                run("supervisorctl -s unix:///tmp/supervisor.sock restart direnaj")

def push_new_changes_deploy_and_restart():
    prepare_deploy()
    deploy()
    setup_environment()
    run_server()

def tail_direnaj():
    with prefix("source /usr/local/bin/virtualenvwrapper.sh"),\
         prefix("workon direnaj"):
        with cd(code_dir):
            run("supervisorctl -s unix:///tmp/supervisor.sock tail direnaj")
