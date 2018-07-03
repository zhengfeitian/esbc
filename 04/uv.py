from flask import Flask, url_for
import re
import json

app = Flask(__name__, static_folder='static', static_url_path='')
spike = ""


with open('index.html') as index_file:
    jerry_ = index_file.readlines()
for line in jerry_:
    spike += line


@app.route('/', methods=['GET', 'POST'])
def index():

    jerry = spike
    try_failed = True
    data = list()
    while try_failed:
        try:
            with open("uv.log") as tom:
                for tyke in tom.readlines():
                    tyke = tyke.strip("\n")
                    if tyke:
                        data.append(tyke)
                try_failed = False
        except:
            try_failed = True

    data_str = json.dumps(data)
    data_str = data_str.replace('"', "")
    jerry = jerry.replace("{{data}}", data_str)
    print(jerry)
    return jerry

if __name__ == "__main__":
    app.run(host="0.0.0.0")

