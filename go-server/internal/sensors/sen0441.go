package sensors

type SEN0441 struct {
	SensorBase
	HCHO struct {
		PPM         float32
		Temperature float32
		Humidity    float32
	}
}
