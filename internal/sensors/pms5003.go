package sensors

type PMS5003 struct {
	SensorBase
	Pm struct {
		Pm1_0 struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"pm1_0"`
		Pm2_5 struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"pm2_5 "`
		Pm10 struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"pm10 "`
		Particles_0_3um struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"particles_0_3um "`
		Particles_0_5um struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"particles_0_5um "`
		Particles_1_0um struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"particles_1_0um "`
		Particles_2_5um struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"particles_2_5um "`
		Particles_5_0um struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"particles_5_0um "`
		Particles_10um struct {
			Unit  string `json:"unit"`
			Value uint16 `json:"value"`
		} `json:"particles_10um "`
	}
}

func (sensor PMS5003) Validate() error {
	return nil
}
