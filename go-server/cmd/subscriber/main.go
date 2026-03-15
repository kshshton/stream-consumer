package main

import (
	"fmt"
	"go-server/internal/options"
	"go-server/internal/processor"
	"os"
	"os/signal"
	"syscall"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

func main() {
	mySensorBuffer := &processor.MessageBuffer{}

	var messageHandler mqtt.MessageHandler = func(client mqtt.Client, msg mqtt.Message) {
		// fmt.Printf("Received message: %s from topic %s\n", msg.Payload(), msg.Topic())
		fmt.Println(mySensorBuffer.Retention(msg, "pms5003", 2))
	}

	opts := options.Connect(&messageHandler)

	client := mqtt.NewClient(opts)
	if token := client.Connect(); token.Wait() && token.Error() != nil {
		panic(token.Error())
	}

	topic := "sensors/#"
	if token := client.Subscribe(topic, 1, nil); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}

	fmt.Printf("Subscribed to %s\n", topic)

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)
	<-sigChan

	client.Disconnect(250)
}
