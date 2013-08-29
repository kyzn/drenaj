import yaml
from string import Template

import argparse

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='configuration generator')
    parser.add_argument('config_yaml_file', help='config file name in yaml format')
    parser.add_argument('config_py_file', help='config file name in yaml format')
    args = parser.parse_args()
    # read config.yaml and generate config.py
    conf = yaml.load(file(args.config_yaml_file, "r"))
    config_document_template = file("direnaj/config.py.tmpl", 'r').read()
    config_py_document = Template(config_document_template).substitute(conf)
    file(args.config_py_file, 'w').write(config_py_document)
