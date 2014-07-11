import yaml
from string import Template

import argparse

import os

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='configuration generator')
    parser.add_argument('config_yaml_file', help='config file name in yaml format')
    parser.add_argument('config_py_file', help='config file name in yaml format')
    args = parser.parse_args()
    # read config.yaml and generate config.py
    conf = yaml.load(file(args.config_yaml_file, "r"))

    # set the project root dir as the current dir.
    conf['project_root_dir'] = os.getcwd()

    # TODO: completely inportabe :) for now.
    # It is not a problem because it is only used by direnajinitdb.py
    # set the directory which mongo binary resides
    conf['mongo_bin_dir'] = '/usr/bin'

    default_conf = yaml.load(file("host-configs/default-configs.yaml", "r"))
    config_document_template = file("direnaj/direnaj_api/config/config.py.tmpl", 'r').read()
    config_py_document = Template(config_document_template).substitute(default_conf, **conf)
    file(args.config_py_file, 'w').write(config_py_document)
