

//FirebaseESP8266.h must be included before ESP8266WiFi.h
#include "FirebaseESP8266.h"  // Install Firebase ESP8266 library
#include <ESP8266WiFi.h>
#include <DHT.h>    // Install DHT11 Library and Adafruit Unified Sensor Library


#define FIREBASE_HOST "android-app-home-default-rtdb.europe-west1.firebasedatabase.app/" //Without http:// or https:// schemes
#define FIREBASE_AUTH "YFWf3Adm1gmu9pe4znOO3mjoYe2nT7udVren5BQs"
#define WIFI_SSID "DNA-WELHO-B282"
#define WIFI_PASSWORD "9F37F6C3C3"

#define DHTPIN D1    // Connect Data pin of DHT to D1

#define DHTTYPE    DHT22
DHT dht(DHTPIN, DHTTYPE);

//Define FirebaseESP8266 data object
FirebaseData firebaseData;


FirebaseJson json;


void setup()
{

  Serial.begin(9600);

  dht.begin();
  
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

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  Firebase.setString(firebaseData, "Room2/Mode", "OFF");



}

void sensorUpdate(){
  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();




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
 


  if (Firebase.setInt(firebaseData, "/Room2/Temp", t))
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

  if (Firebase.setInt(firebaseData, "/Room2/Hum", h))
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

  
}
void loop() {
  
  
  if (Firebase.getString(firebaseData, "Room2/Mode")){
    Serial.println(firebaseData.stringData());
    if (firebaseData.stringData() == "ON") {
      sensorUpdate();
      Firebase.setString(firebaseData, "Room2/Mode", "OFF");

      
    }
  else if (firebaseData.stringData() == "OFF"){
    Serial.println("Sensor update OFF");
    }
  }
  Serial.println("loop");
  delay(1000);
  
}
