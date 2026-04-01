package sensors

type PMS5003 struct {
	SensorBase
	Pm struct {
		Pm1_0           float32
		Pm2_5           float32
		Pm10            float32
		Particles_0_3um float32
		Particles_0_5um float32
		Particles_1_0um float32
		Particles_2_5um float32
		Particles_5_0um float32
		Particles_10um  float32
	}
}

func (sensor PMS5003) Validate() error {
	return nil
}
