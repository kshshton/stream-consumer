package sensors

import "time"

type SensorBase struct {
	SensorID  int8
	Timestamp time.Time
	Location  string
}
