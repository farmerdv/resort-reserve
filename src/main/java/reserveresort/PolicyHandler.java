package reserveresort;

import reserveresort.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    @Autowired
    ReserveRepository reserveRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverConfimed_UpdateStatus(@Payload Confimed confimed){

        System.out.println("##### wheneverConfimed_UpdateStatus : " + confimed.toJson());

        if(confimed.isMe()){
            Reserve reserve = new Reserve();
            reserve.setStatus("Confirmed");
            reserve.setResortId(confimed.getResortId());
            reserve.setRoomId(confimed.getRoomId());
            reserve.setPeopleCount(confimed.getPeopleCount());
            reserveRepository.findById(confimed.getId());
            reserveRepository.save(reserve);

            System.out.println("##### listener UpdateStatus : " + confimed.toJson());
        }
    }

}
