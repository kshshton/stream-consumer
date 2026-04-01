package sensors

type SEN0441 struct {
	SensorBase
	HCHO struct {
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

func (sensor SEN0441) Validate() error {
	return nil
}
