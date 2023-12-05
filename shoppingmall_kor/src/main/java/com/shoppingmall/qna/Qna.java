package com.shoppingmall.qna;

import com.shoppingmall.item.Item;
import com.shoppingmall.users.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Qna")
public class Qna {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QNA_SEQ") // 추가
	@SequenceGenerator(name = "QNA_SEQ", sequenceName = "QNA_SEQ", allocationSize = 1) // 추가
	@Column(name = "qnaid")
	private Long qnaid;
	@Column(name = "question")
	private String question;
	@Column(name = "answer")
	private String answer;

	@JoinColumn(name = "usersid")
	private Long usersid;

	@JoinColumn(name = "itemid")
	private Long itemid;

	@ManyToOne
	@JoinColumn(name = "usersid", referencedColumnName = "usersid", insertable = false, updatable = false)
	public Users users;

	@ManyToOne
	@JoinColumn(name = "itemid", referencedColumnName = "itemid", insertable = false, updatable = false)
	public Item Item;





	public Qna() {
	}

	public Qna(String question, String answer, Long usersid, Long itemid) {
		this.question = question;
		this.answer = answer;
		this.usersid = usersid;
		this.itemid = itemid;
		
	}

	public Long getUsersid() {
		return usersid;
	}

	public void setUsersid(Long usersid) {
		this.usersid = usersid;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public Long getQnaid() {
		return qnaid;
	}

	public void setQnaid(Long qnaid) {
		this.qnaid = qnaid;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

// -- 추가 ---------------//
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Item getItem() {
		return Item;
	}

	public void setItem(Item item) {
		Item = item;
	}


// ---------------------//

	@Override
	public String toString() {
		return "Qna [qnaid=" + qnaid + ", question=" + question + ", answer=" + answer + ", usersid=" + usersid
				+ ", itemid=" + itemid + "]";
	}



}
