package sensors

type SCD41 struct {
	SensorBase
	CO2 struct {
		PPM         float32
		Temperature float32
		Humidity    float32
	}
}

func (sensor SCD41) Validate() error {
	return nil
}
