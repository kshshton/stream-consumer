package main

import (
	"fmt"
	"go-server/internal/config"
	"go-server/internal/processor"
	"go-server/internal/sensors"
	"net/http"
	"os"
	"os/signal"
	"syscall"

	mqtt "github.com/eclipse/paho.mqtt.golang"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

var (
	mqttMessagesProcessed = promauto.NewCounterVec(prometheus.CounterOpts{
		Name: "mqtt_messages_processed_total",
		Help: "Total number of MQTT messages processed by the Go server",
	}, []string{"sensor_type"})
)

func main() {
	go func() {
		http.Handle("/metrics", promhttp.Handler())
		fmt.Println("Prometheus metrics available at :8080/metrics")
		if err := http.ListenAndServe(":8080", nil); err != nil {
			fmt.Printf("Metrics server failed: %v\n", err)
		}
	}()

	// mySensorBuffer := &processor.MessageBuffer{}

	sen0441Ch := sensors.Registry["sen0441"].Channel
	scd41Ch := sensors.Registry["scd41"].Channel
	pms5003Ch := sensors.Registry["pms5003"].Channel
	ens160Ch := sensors.Registry["ens160"].Channel
	bme280Ch := sensors.Registry["bme280"].Channel
	ds18b20Ch := sensors.Registry["ds18b20"].Channel

	go func() {
		for {
			select {
			case data := <-sen0441Ch:
				fmt.Printf("sen0441: %v", data)
			case data := <-scd41Ch:
				fmt.Printf("scd41Ch: %v", data)
			case data := <-pms5003Ch:
				fmt.Printf("pms5003Ch: %v", data)
			case data := <-ens160Ch:
				fmt.Printf("ens160Ch: %v", data)
			case data := <-bme280Ch:
				fmt.Printf("bme280Ch: %v", data)
			case data := <-ds18b20Ch:
				fmt.Printf("ds18b20Ch: %v", data)
			}
		}
	}()

	var messageHandler mqtt.MessageHandler = func(client mqtt.Client, msg mqtt.Message) {
		processor.Deserializer(msg)
		// fmt.Println(mySensorBuffer.Retention(msg, "pms5003", 2))
	}

	opts := config.Connect(&messageHandler)

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
