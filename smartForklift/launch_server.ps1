Get-ChildItem ./ | Where{$_.LastWriteTime -lt (Get-Date).AddDays(-3) }  | Where {$_.Name -match '.*parcel.*.pdf'} | ForEach-Object { Remove-Item -LiteralPath $_.Name }
python3 -m venv ENV
ENV\Scripts\activate
pip3 install -r requirements-lock.txt
$Env:FLASK_APP="web.py" 
$Env:FLASK_DEBUG=1
$Env:FLASK_RUN_PORT=5001
flask run --host=0.0.0.0