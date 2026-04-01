package sensors

import (
	"fmt"
)

type SensorInfo struct {
	Factory func() any
	Channel chan any
}

var Registry = map[string]*SensorInfo{
	"sen0441": {Factory: func() any { return &SEN0441{} }},
	"scd41":   {Factory: func() any { return &SCD41{} }},
	"pms5003": {Factory: func() any { return &PMS5003{} }},
	"ens160":  {Factory: func() any { return &ENS160{} }},
	"bme280":  {Factory: func() any { return &BME280{} }},
	"ds18b20": {Factory: func() any { return &DS18B20{} }},
}

func init() {
	fmt.Print("Called init...")
	for _, info := range Registry {
		info.Channel = make(chan any, 10)
	}
}
