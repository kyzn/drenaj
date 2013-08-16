sudo apt-get install python-pip

sudo pip install virtualenvwrapper

cat > ~/.virtualenvrc-direnaj <<HERE
export WORKON_HOME=$HOME/.virtualenvs
source /usr/local/bin/virtualenvwrapper.sh
HERE

if fgrep -q 'source ~/.virtualenvrc-direnaj' ~/.bashrc || [[ -v WORKON_HOME ]]; then 
    echo 'virtualenvwrapper is already installed';
else 
    echo 'virtualenvwrapper is installed';
    cat >> ~/.bashrc <<HERE2

# direnaj
source ~/.virtualenvrc-direnaj

HERE2

source ~/.virtualenvrc-direnaj

fi

mkvirtualenv direnaj

#m set the project root dir
export PROJECT_ROOT_DIR=`pwd` && sed "s@^\(PROJECT_ROOT_DIR=\).*@\1'$PROJECT_ROOT_DIR'@g" src/config.py.tmpl > src/config.py

