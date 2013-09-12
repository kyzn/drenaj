from __future__ import with_statement
from fabric.api import local, settings, run, abort, env, cd, prefix
from fabric.contrib.console import confirm

#env.forward_agent = True
#env.gateway = 'onur@lsn'

env.direnaj = {}
env.direnaj['hostname'] = 'voltran'
env.hosts = ['direnaj@%s' % env.direnaj['hostname']]

env.use_ssh_config = True

env.direnaj['environment'] = 'staging'

env.direnaj['deployment_repo_remote_name'] = 'deployment_repo_cank'
env.direnaj['repo_user'] = 'onur'
env.direnaj['repo_uri'] = 'ssh://%s@78.47.211.238//home/can/siteler/direnaj/git_repos/code' % env.direnaj['repo_user']

env.direnaj['supervisor_socket_path'] = '/var/run/supervisor.sock'

env.direnaj['code_dir'] = '/home/direnaj/direnaj/envs/%s' % env.direnaj['environment']

def init(environment=env.direnaj['environment'], hostname=env.direnaj['hostname']):

    env.direnaj['hostname'] = hostname
    env.direnaj['environment'] = environment

    env.direnaj['code_dir'] = '/home/direnaj/direnaj/envs/%s' % env.direnaj['environment']

    print env.direnaj

def test():
    with settings(warn_only=True):
        result = local('py.test -q', capture=True)
    if result.failed and not confirm("Tests failed. Continue anyway?"):
        abort("Aborting at user request.")

def setup_deployment_repo():

    ensure_apt_package("git")

##`    with settings(warn_only=True):
##`        if run("test -d %s" % env.direnaj['repo_dir']).failed:
##`            run("git init --bare %s" % env.direnaj['repo_dir'])
    with settings(warn_only=True), cd(env.direnaj['code_dir']):
        result = run("git remote | grep ^%s$" % env.direnaj['deployment_repo_remote_name'])
        if result.failed:
            run("git remote add %s %s" % (env.direnaj['deployment_repo_remote_name'], env.direnaj['repo_uri']))
            run("git fetch %s" % (env.direnaj['deployment_repo_remote_name']))
    with settings(warn_only=True):
        result = local("git remote | grep ^%s$" % env.direnaj['deployment_repo_remote_name'])
        if result.failed:
            local("git remote add %s %s" % (env.direnaj['deployment_repo_remote_name'], env.direnaj['repo_uri']))
            local("git fetch %s" % (env.direnaj['deployment_repo_remote_name']))
##`    with settings(warn_only=True):
##`        result = run("git remote | grep ^deployment_repo_%s$" % env.direnaj['cank'])
##`        if result.failed:
##`            run("git remote add deployment_repo_%s %s" % (env.direnaj['cank'], env.direnaj['cank_repo_uri']))

def push():
    local("git push %s %s_deployment" % (env.direnaj['deployment_repo_remote_name'], env.direnaj['environment']))

def prepare_deploy(environment=env.direnaj['environment'],
                   hostname=env.direnaj['hostname']):
    init(environment, hostname)
    #test()
    setup_deployment_repo()
    push()

def deploy():
    with settings(warn_only=True):
        if run("test -d %s" % env.direnaj['code_dir']).failed:
            run("git clone %s %s" % (env.direnaj['repo_uri'], env.direnaj['code_dir']))
    with cd(env.direnaj['code_dir']):
        run("git pull %s %s_deployment" % (env.direnaj['deployment_repo_remote_name'], env.direnaj['environment']))
        run("git checkout %s_deployment" % env.direnaj['environment'])

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

    ensure_apt_package("supervisor")

    # ensure nginx
    ensure_nginx()

    # ensure rabbitmq
    ensure_rabbitmq()

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
        with cd(env.direnaj['code_dir']):
            run("pip install -r env/env_requirements.txt")
            run("python configure.py host-configs/config-%s-%s.yaml direnaj/config.py" % (env.direnaj['hostname'], env.direnaj['environment']))

    # TODO: think about db initialization. it's manual right now.

def ensure_rabbitmq():

    with settings(warn_only=True):
        result = run("test -f /etc/apt/sources.list.d/rabbitmq.list")
    if result.failed:
        with cd('/tmp'):
            run("wget http://www.rabbitmq.com/rabbitmq-signing-key-public.asc")
            run("sudo apt-key add rabbitmq-signing-key-public.asc")
            run("rm rabbitmq-signing-key-public.asc")
            if result.failed:
                run("su -c 'echo deb http://www.rabbitmq.com/debian/ testing main  >> /etc/apt/sources.list.d/rabbitmq.list'")
            run("sudo apt-get update")
            run('DEBIAN_FRONTEND=noninteractive sudo apt-get -q --yes -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" install %s' % "rabbitmq-server")

def ensure_jenkins():

    with settings(warn_only=True):
        result = run("test -f /etc/apt/sources.list.d/jenkins.list")
    if result.failed:
        with cd('/tmp'):
            run("wget http://pkg.jenkins-ci.org/debian/jenkins-ci.org.key")
            run("sudo apt-key add jenkins-ci.org.key")
            run("rm jenkins-ci.org.key")
            if result.failed:
                run("su -c 'echo deb http://pkg.jenkins-ci.org/debian binary/ >> /etc/apt/sources.list.d/jenkins.list'")
            run("sudo apt-get update")
            run('DEBIAN_FRONTEND=noninteractive sudo apt-get -q --yes -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" install %s' % "jenkins")


def ensure_nginx():

    with settings(warn_only=True):
        result = run("test -f /etc/apt/sources.list.d/nginx.list")
    if result.failed:
        with cd('/tmp'):
            run("wget http://nginx.org/keys/nginx_signing.key")
            run("sudo apt-key add nginx_signing.key")
            run("rm nginx_signing.key")
            if result.failed:
                run("su -c 'echo deb http://nginx.org/packages/debian/ wheezy nginx >> /etc/apt/sources.list.d/nginx.list'")
                run("su -c 'echo deb-src http://nginx.org/packages/debian/ wheezy nginx >> /etc/apt/sources.list.d/nginx.list'")
            run("sudo apt-get update")
            run('DEBIAN_FRONTEND=noninteractive sudo apt-get -q --yes -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" install %s' % "nginx")

    # TODO: not well thought for now.
    #with cd(env.direnaj['code_dir']):
    #    put("host-configs/nginx/direnaj.conf", "/etc/nginx/conf.d/direnaj.conf")

def run_server():
    # ensure mongodb is running
    # ensure nginx is running
    # ensure rabbitmq is running
    # bla bla... maybe use supervisord is it possible?
    # start the main direnaj process
    with prefix("source /usr/local/bin/virtualenvwrapper.sh"),\
         prefix("workon direnaj"):
        with cd(env.direnaj['code_dir']):
            # make sure there is a logs dir.
            run("mkdir -p logs")
            with settings(warn_only=True):
                result = run("sudo service supervisor status")
                #result = run("test -S %s" % env.direnaj['supervisor_socket_path'])
            if result.failed:
                with prefix("sudo service supervisor start"):
                #with prefix("supervisord -c supervisord.conf"):
                    run("supervisorctl -s unix://%s restart direnaj_%s" % (env.direnaj['supervisor_socket_path'], env.direnaj['environment']))
            else:
                run("supervisorctl -s unix://%s restart direnaj_%s" % (env.direnaj['supervisor_socket_path'], env.direnaj['environment']))

def push_new_changes_deploy_and_restart():
    prepare_deploy()
    deploy()
    setup_environment()
    run_server()

def tail_direnaj(environment=env.direnaj['environment']):
    with prefix("source /usr/local/bin/virtualenvwrapper.sh"),\
         prefix("workon direnaj"):
        with cd(node_dir):
            run("supervisorctl -s unix://%s tail direnaj_%s" % (env.direnaj['supervisor_socket_path'], environment))
