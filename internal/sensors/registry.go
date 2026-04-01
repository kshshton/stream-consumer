package sensors

import (
	"fmt"
	schemas "go-server/internal/sensors/schemas"
)

type SensorInfo struct {
	Factory func() any
	Channel chan any
}

var Registry = map[string]*SensorInfo{
	"sen0441": {Factory: func() any { return &schemas.SEN0441{} }},
	"scd41":   {Factory: func() any { return &schemas.SCD41{} }},
	"pms5003": {Factory: func() any { return &schemas.PMS5003{} }},
	"ens160":  {Factory: func() any { return &schemas.ENS160{} }},
	"bme280":  {Factory: func() any { return &schemas.BME280{} }},
	"ds18b20": {Factory: func() any { return &schemas.DS18B20{} }},
}

func init() {
	fmt.Print("Called init...")
	for _, info := range Registry {
		info.Channel = make(chan any, 10)
	}
}
