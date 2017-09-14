
source "virtualenv/bin/activate"

pip install -r requirements.txt
cd efluent_webserver

python manage.py migrate
python manage.py runserver
