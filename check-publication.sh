read -p "version to check > " version
echo "entered version = $version"
url="https://repo1.maven.org/maven2/me/bvn13/sdk/android/gpx/GpxAndroidSdk/${version}/"
echo "checking $url"

check(){
  echo $(curl -sL -w "%{http_code}\\n" "$url" -o /dev/null)
}

count=0
until [ $count -gt 1000 ] || [ "$(check)" == "200" ]
do
  code=$(check)
  echo "try $count - response $code"
  ping -c 2 127.0.0.1 > /dev/null
  count=$((count+1))
done

code=$(check)
if [ "$code" == "200" ]
then
  echo "$code is 200"
  notify-send -a MAVEN -t 500 'GpxAndroidSdk' "version $version is published"
else
  echo "$code is not 200"
  notify-send -a MAVEN -t 500 -u critical 'GpxAndroidSdk' "failed to publish $version version"
fi