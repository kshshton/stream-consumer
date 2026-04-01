package sensors

// Type: Temperature Sensor
type DS18B20 struct {
	SensorBase
	Temperature struct {
		Unit  string  `json:"unit"`
		Value float32 `json:"value"`
	} `json:"temperature"`
}

func (sensor DS18B20) Validate() error {
	return nil
}
