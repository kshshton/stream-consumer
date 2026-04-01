package sensors

import "time"

type SensorBase struct {
	SensorID  int8      `json:"sensorId"`
	Timestamp time.Time `json:"timestamp"`
	Location  string    `json:"location"`
}

func (sensor SensorBase) Validate() error {
	return nil
}
