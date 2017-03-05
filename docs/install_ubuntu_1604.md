
## Installing on Ubuntu 16.04

 - Clone the repository, preferably to your home folder `~`, by issuing the following command. Answer "yes" if fingerprint question about the authenticity of host comes up.

```bash
git clone https://github.com/boun-cmpe-soslab/drenaj.git
```

 - Install dependencies by running following commands. 

```bash
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install build-essential python python-pip python-dev ntp ntpdate
sudo pip install virtualenvwrapper
```

 - Run following commands to modify and run `.bashrc`.

```bash
echo -e 'export WORKON_HOME=$HOME/.virtualenvs\nsource /usr/local/bin/virtualenvwrapper.sh' >> ~/.bashrc
source ~/.bashrc
```

  - Rename configuration template as configuration file.

```bash
mv ~/drenaj/drenaj/client/config/config.py.tmpl ~/drenaj/drenaj/client/config/config.py
```

  - Optional: If you want to tweets to be saved to a file for external processing, you need to edit `~/drenaj/drenaj/client/workers/streamcatcher.py`. Find the `post_to_gateway` function, and append the following right after `if not tmp: return`, without changing the indentation level.

```python3
        f = open("/tmp/stream_out.json", "a")
        for tweet in tmp:
            tweet['campaign_id']=params['campaign_id']
            f.write(bson.json_util.dumps(tweet))
            f.write("\n")
        f.close()
```

  - Before going further, it is suggested that you launch a tmux window and operate under that.

```bash
tmux
```

  - Make sure you are at the correct directory.

```bash
cd ~/drenaj
```

  - Now, let's create a virtual environment. After this is done, you should see `(drenaj_client)` at the beginning of the line.

```bash
mkvirtualenv drenaj_client
```

  - Call `client_setup.py` as follows. It might give a warning about installing libzmq, let the script install it. You may also observe warnings for C/C++ objects, that you can ignore. This might take a few minutes.

```bash
python client_setup.py install
```

  - After `client_setup` is done, now you can finally start drenaj for the first time. To do so, issue the following command from the very same `~/drenaj` directory. 

```bash
drenaj_client setup
```

  - As soon as you issue the command, it will ask you to authorize yourself through Twitter by providing a link. Visit the link, authenticate yourself and put the pincode back to terminal. You can add as many Twitter accounts as you like. When you want to stop, just give empty pincode. When you finish authenticating, you should see `Direnaj Local Visualization and Interaction Manager Starting on port 19999` on your tmux screen. 

  - You can visit [http://localhost:19999/campaigns/list](http://localhost:19999/campaigns/list) to create/spawn campaigns, and [http://direnaj-staging.cmpe.boun.edu.tr](http://direnaj-staging.cmpe.boun.edu.tr) to see the results.

  - To keep drenaj running, detach tmux by pressing `CTRL B` at the same time, and then pressing `D`. You can always attach it back by `tmux at` command.

  - To exit drenaj, press `CTRL C`. If it doesn't work, use `Ctrl Z` and run `kill %1`. Leave virtual environment by running `deactivate`.

  - You can check whether drenaj is still running or not by issuing `pgrep drenaj`. If it gives you a number, that will be the process id drenaj is running under.





## Running on Ubuntu 16.04

  - Get into a `tmux` screen, and make sure you are at the right directory.

```bash
tmux
cd ~/drenaj
```

  - Get into virtual environment.

```bash
workon drenaj_client
```

  - Run drenaj.

```bash
drenaj_client runserver
```



## Seeing all tweets in JSON

  - Using Google Chrome, visit [http://direnaj-staging.cmpe.boun.edu.tr](http://direnaj-staging.cmpe.boun.edu.tr) and load the campaign page of interest.
  - Right click the page once loaded, and click `inspect element`. This will make developer tools visible.
  - Find the `network` tab in developer tools, and select it. Then, reload the page.
  - You should see a `filter` item in network tab. Right click it, and select open in new tab.
  - You can use [Chrome Extensions](https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc?hl=en) for displaying pretty JSON.