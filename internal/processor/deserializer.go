package processor

import (
	"encoding/json"
	"fmt"
	"go-server/internal/sensors"
	"strings"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

func Deserializer(msg mqtt.Message) {
	var sensorName string

	topicElements := strings.Split(msg.Topic(), "/")
	if len(topicElements) > 1 {
		sensorName = topicElements[1]
	} else {
		err := fmt.Errorf("Wrong format of topic: %s", msg.Topic())
		fmt.Println(err.Error())
	}

	info, ok := sensors.Registry[sensorName]
	if !ok {
		err := fmt.Errorf("\nUnknown sensor: %s", msg.Topic())
		fmt.Println(err.Error())
	}

	sensorInstance := info.Factory()

	if err := json.Unmarshal(msg.Payload(), sensorInstance); err != nil {
		fmt.Println(err.Error())
	}

	info.Channel <- sensorInstance
}
