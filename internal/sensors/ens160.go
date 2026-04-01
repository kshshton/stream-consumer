package sensors

// Type: Indoor Air Quality Sensor
type ENS160 struct {
	SensorBase
	Iaq struct {
		VocIndex struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"vocIndex"`

		Eco2 struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"eco2"`

		Temperature struct {
			Unit  string  `json:"unit"`
			Value float32 `json:"value"`
		} `json:"temperature"`

		Humidity struct {
			Unit  string  `json:"unit"`
			Value float32 `json:"value"`
		} `json:"humidity"`
	}
}

func (sensor ENS160) Validate() error {
	return nil
}
