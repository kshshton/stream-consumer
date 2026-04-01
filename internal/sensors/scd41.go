package sensors

type SCD41 struct {
	SensorBase
	CO2 struct {
		PPM struct {
			Unit  string  `json:"unit"`
			Value float32 `json:"value"`
		} `json:"ppm"`

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

func (sensor SCD41) Validate() error {
	return nil
}
