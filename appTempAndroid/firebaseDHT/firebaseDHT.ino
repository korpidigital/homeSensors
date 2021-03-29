

//FirebaseESP8266.h must be included before ESP8266WiFi.h
#include "FirebaseESP8266.h"  // Install Firebase ESP8266 library
#include <ESP8266WiFi.h>
#include <DHT.h>    // Install DHT11 Library and Adafruit Unified Sensor Library
#include <NTPClient.h>
#include <WiFiUdp.h>


#define FIREBASE_HOST "hostname" //Without http:// or https:// schemes
#define FIREBASE_AUTH "auth"
#define WIFI_SSID "DNA-WELHO-B282"
#define WIFI_PASSWORD "pass"


#define DHTPIN D1    // Connect Data pin of DHT to D2
int led = D5;     // Connect LED to D5

#define DHTTYPE    DHT22
DHT dht(DHTPIN, DHTTYPE);

//Define FirebaseESP8266 data object
FirebaseData firebaseData;
FirebaseData arduinoData;

const long utcOffsetInSeconds = 7200;
// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", utcOffsetInSeconds);


void setup()
{

  Serial.begin(9600);

  dht.begin();
  pinMode(led,OUTPUT);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
  timeClient.begin();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  Firebase.setString(arduinoData, "Room1/Mode", "OFF");

}

void sensorUpdate(){
  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();

  timeClient.update();
  unsigned long epochTime = timeClient.getEpochTime();
  
  String currentHour = String(timeClient.getHours());
  if(currentHour.length() < 2){
    currentHour = "0"+currentHour;
  }
  String currentMinute = String(timeClient.getMinutes());
  if(currentMinute.length() < 2){
    currentMinute = "0"+currentMinute;
  }
  String Time = String(currentHour)+":"+String(currentMinute);
  Serial.println(Time); 

  struct tm *ptm = gmtime ((time_t *)&epochTime); 
  String monthDay = String(ptm->tm_mday);
  String currentMonth = String(ptm->tm_mon+1);
  int currentYear = ptm->tm_year+1900;
  String currentDate = String(monthDay) + "-" + String(currentMonth) + "-" + String(currentYear);
  Serial.println(currentDate);


  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }

  Serial.print(F("Humidity: "));
  Serial.print(h);
  Serial.print(F("%  Temperature: "));
  Serial.print(t);
  Serial.print(F("C  ,"));
 


  if (Firebase.setInt(firebaseData, "/Room1/Temp", t))
  {
    Serial.println("PASSED");
    Serial.println("PATH: " + firebaseData.dataPath());
    Serial.println("TYPE: " + firebaseData.dataType());
    Serial.println("ETag: " + firebaseData.ETag());
    Serial.println("------------------------------------");
    Serial.println();
  }
  else
  {
    Serial.println("FAILED");
    Serial.println("REASON: " + firebaseData.errorReason());
    Serial.println("------------------------------------");
    Serial.println();
  }

  if (Firebase.setInt(firebaseData, "/Room1/Hum", h))
  {
    Serial.println("PASSED");
    Serial.println("PATH: " + firebaseData.dataPath());
    Serial.println("TYPE: " + firebaseData.dataType());
    Serial.println("ETag: " + firebaseData.ETag());
    Serial.println("------------------------------------");
    Serial.println();
  }
  else
  {
    Serial.println("FAILED");
    Serial.println("REASON: " + firebaseData.errorReason());
    Serial.println("------------------------------------");
    Serial.println();
  }
  Firebase.setString(arduinoData, "Time", Time);
  Firebase.setString(arduinoData, "Date", currentDate);

}
void loop() {
  
  if (Firebase.getString(arduinoData, "Room1/Mode")){
    Serial.println(arduinoData.stringData());
    if (arduinoData.stringData() == "ON") {
      sensorUpdate();
      Firebase.setString(arduinoData, "Room1/Mode", "OFF");
      
    }
  else if (arduinoData.stringData() == "OFF"){
    Serial.println("Sensor update OFF");
    }
  }
  Serial.println("loop");
  
  delay(1000);
  
}
