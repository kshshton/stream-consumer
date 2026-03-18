package processor

import (
	"encoding/json"
	"fmt"
	"go-server/internal/sensors"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

func display(stream []byte, sensor any) {
	json.Unmarshal(stream, &sensor)
	fmt.Println(sensor)
}

func Deserializer(msg mqtt.Message) {
	var ds18b20 sensors.DS18B20
	var bme280 sensors.BME280
	var ens160 sensors.ENS160
	var pms5003 sensors.PMS5003
	var scd41 sensors.SCD41
	var sen0441 sensors.SEN0441

	switch msg.Topic() {
	case "sensors/ds18b20/ds18b20_01":
		display(msg.Payload(), ds18b20)
	case "sensors/bme280/bme280_01":
		display(msg.Payload(), bme280)
	case "sensors/ens160/ens160_01":
		display(msg.Payload(), ens160)
	case "sensors/pms5003/pms5003_01":
		display(msg.Payload(), pms5003)
	case "sensors/scd41/scd41_01":
		display(msg.Payload(), scd41)
	case "sensors/sen0441/sen0441_01":
		display(msg.Payload(), sen0441)
	}
}
