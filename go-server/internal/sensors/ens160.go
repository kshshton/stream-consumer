package sensors

type ENS160 struct {
	SensorBase
	Iaq struct {
		VocIndex    int8
		Eco2        int8
		Temperature float32
		Humidity    float32
	}
}
