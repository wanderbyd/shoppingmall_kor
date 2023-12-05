
package com.shoppingmall.review;


import com.shoppingmall.item.Item;
import com.shoppingmall.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
//import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Review")
public class Review {
 
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVIEW_SEQ")
    @SequenceGenerator(name = "REVIEW_SEQ", sequenceName = "REVIEW_SEQ", allocationSize = 1)
    @Column(name = "rvid")
    private Long rvid;
    
    @Column(name = "contents")
    private String contents;

    
    @ManyToOne
    @JoinColumn(name = "usersid", referencedColumnName = "usersid", insertable = true, updatable = true)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "itemid", referencedColumnName = "itemid", insertable = true, updatable = true)
    private Item item;
    
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISPLAY_NUMBER")
    private Integer displayNumber;
    

    @Lob
    @Column(name = "review_image", columnDefinition = "BLOB", nullable = true)
    private byte[] reviewImage;
    
    @Column(name = "likes", nullable = true)
    private Integer likes;



	public Review() {}


	public Review(String contents, Users users, Item item,Integer likes) {
	    this.contents = contents;
	    this.users = users;
	    this.item = item;
	    this.likes = likes;
	}
	
	
	public Long getRvid() {
		return rvid;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item =item;
	}

	public void setRvid(Long rvid) {
		this.rvid = rvid;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	
	public Integer getDisplayNumber() {
        return displayNumber;
    }

    public void setDisplayNumber(Integer displayNumber) {
        this.displayNumber = displayNumber;
    }


 //이거 활성화 시키면 시간 갭을 줘서 새 리뷰로 뜨도록 //지우지 말것
	public void setDisplayNumber(int i) {

	}

	
	@Override
	public String toString() {
		return "Review [rvid=" + rvid + ", contents=" + contents +
				", users=" + users + ", item=" + item +
				"]";
	}


	public byte[] getReviewImage() {
		return reviewImage;
	}


	public void setReviewImage(byte[] reviewImage) {
		this.reviewImage = reviewImage;
	}

	public Integer getLikes() {
	    return this.likes != null ? this.likes.intValue() : 0; // 예시로 0을 기본값으로 사용
	}


	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	
	
}



