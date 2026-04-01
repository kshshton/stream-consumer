package sensors

// Type: Environmental Sensor
type BME280 struct {
	SensorBase
	Environment struct {
		Temperature struct {
			Unit  string  `json:"unit"`
			Value float32 `json:"value"`
		} `json:"temperature"`

		Humidity struct {
			Unit  string  `json:"unit"`
			Value float32 `json:"value"`
		} `json:"humidity"`

		Pressure struct {
			Unit  string  `json:"unit"`
			Value float32 `json:"value"`
		} `json:"pressure"`
	}
}

func (sensor BME280) Validate() error {
	return nil
}
