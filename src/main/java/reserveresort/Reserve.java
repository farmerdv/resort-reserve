package reserveresort;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Reserve_table")
public class Reserve {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String resortId;
    private Integer peopleCount;
    private String roomId;
    private String status;

    @PostPersist
    public void onPostPersist(){

        System.out.println("#####34asdfasdf status" + this.status);

        if("Reserve".equals(this.status)) {

            System.out.println("##### status" + this.status);
            Reserved reserved = new Reserved();
            reserved.setResortId(this.getResortId());
            reserved.setRoomId(this.getRoomId());
            reserved.setPeopleCount(this.getPeopleCount());
            reserved.setStatus(this.getStatus());

            BeanUtils.copyProperties(this, reserved);
            reserved.publishAfterCommit();
        }
    }

    @PreRemove
    public void onPreRemove(){
        ReserveCanceled reserveCanceled = new ReserveCanceled();
        BeanUtils.copyProperties(this, reserveCanceled);
        reserveCanceled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        reserveresort.external.Cancellation cancellation = new reserveresort.external.Cancellation();
        cancellation.setReserveId(String.valueOf(this.getId()));
        cancellation.setStatus("Cancelled");
        // mappings goes here
        ReserveApplication.applicationContext.getBean(reserveresort.external.CancellationService.class)
            .cancel(cancellation);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getResortId() {
        return resortId;
    }

    public void setResortId(String resortId) {
        this.resortId = resortId;
    }
    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
