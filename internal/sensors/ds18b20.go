package sensors

type DS18B20 struct {
	SensorBase
	Temperature float32
}

func (sensor DS18B20) Validate() error {
	return nil
}
