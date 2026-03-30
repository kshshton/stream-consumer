package sensors

type BME280 struct {
	SensorBase
	Environment struct {
		Temperature float32
		Humidity    float32
		Pressure    float32
	}
}
