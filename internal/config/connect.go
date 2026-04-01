package options

import mqtt "github.com/eclipse/paho.mqtt.golang"

func Connect(messageHandler *mqtt.MessageHandler) *mqtt.ClientOptions {
	opts := mqtt.NewClientOptions()
	opts.AddBroker("tcp://localhost:1883")
	opts.SetClientID("mqtt-client")
	opts.SetDefaultPublishHandler(*messageHandler)
	return opts
}
