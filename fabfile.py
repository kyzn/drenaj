from __future__ import with_statement
from fabric.api import local, settings, run, abort, env, cd, prefix
from fabric.contrib.console import confirm

#env.forward_agent = True
#env.gateway = 'onur@lsn'

env.hosts = ['direnaj@integrator']

env.use_ssh_config = True

code_dir = '/home/direnaj/direnaj/envs/staging'

def test():
    with settings(warn_only=True):
        result = local('py.test -q', capture=True)
    if result.failed and not confirm("Tests failed. Continue anyway?"):
        abort("Aborting at user request.")

def push():
    local("git push deployment_repo master")

def prepare_deploy():
    test()
    push()

def deploy():
    with settings(warn_only=True):
        if run("test -d %s" % code_dir).failed:
            run("git clone /home/direnaj/direnaj/repo %s" % code_dir)
    with cd(code_dir):
        run("git pull")

def setup_environment():

    # ensure pip is installed.

    virtualenv_dir = '/home/direnaj/.virtualenvs/direnaj/'
    with settings(warn_only=True):
        result = run("test -d %s" % virtualenv_dir)
    with prefix("source /usr/local/bin/virtualenvwrapper.sh"):
        if result.failed:
            run("mkvirtualenv direnaj")

    # ensure libcurl-dev is installed. it might be libcurl4-openssl-dev

    with prefix("source /usr/local/bin/virtualenvwrapper.sh"),\
         prefix("workon direnaj"):
        with cd(code_dir):
            run("pip install -r env/env_requirements.txt")

#def run_server():
#    with prefix("source /usr/local/bin/virtualenvwrapper.sh"),\
#         prefix("workon direnaj"):
#        with cd(code_dir):
