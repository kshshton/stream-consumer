package processor

import (
	"strings"
	"sync"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type Message struct {
	Topic   string
	Payload []byte
}

type MessageBuffer struct {
	sync.Mutex
	Data []Message
}

func (mb *MessageBuffer) Retention(msg mqtt.Message, topic string, messageCapacity int8) []Message {
	mb.Lock()
	defer mb.Unlock()

	if !strings.Contains(msg.Topic(), topic) {
		return nil
	}

	if len(mb.Data) >= int(messageCapacity) {
		mb.Data = mb.Data[1:]
	}

	mb.Data = append(mb.Data, Message{Topic: msg.Topic(), Payload: msg.Payload()})
	return mb.Data
}
