package processor

import (
	"encoding/json"
	"fmt"
	"go-server/internal/sensors"
	"strings"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

func Deserializer(msg mqtt.Message) {
	sensorName := strings.Split(msg.Topic(), "/")[1]

	info, ok := sensors.Registry[sensorName]
	if !ok {
		fmt.Printf("\nUnknown sensor: %s", msg.Topic())
	}

	sensorInstance := info.Factory()

	if err := json.Unmarshal(msg.Payload(), sensorInstance); err != nil {
		fmt.Println(err)
	}

	info.Channel <- sensorInstance
}
