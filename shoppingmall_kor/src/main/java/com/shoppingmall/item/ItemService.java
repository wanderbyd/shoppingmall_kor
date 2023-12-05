package com.shoppingmall.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

// 아이템생성
	public Item createItem(Item item) {
		return itemRepository.save(item);
	}

// 아이템찾기
	public Optional<Item> findItemById(Long itemid) {
		return itemRepository.findByItemid(itemid);
	}

// 전체아이템찾기
	public List<Item> findAllItems() {
		return itemRepository.findAll();
	}

// 아이템 업데이트
	public Item updateItem(Item item) {
		return itemRepository.save(item);
	}

// 아이템삭제
	public void deleteItem(Long itemid) {
		itemRepository.deleteById(itemid);
	}

// 가격범위로찾기
	public List<Item> findItemsByPriceRange(Long min, Long max) {
		return itemRepository.findByPriceBetween(min, max);
	}

// 키워드로 찾기
	public List<Item> searchItemsByKeyword(String keyword) {
		return itemRepository.findByKeywordLike(keyword);
	}

	// 카테고리별로 보기
	public List<Item> searchItemsByCategory(Category category) {
		return itemRepository.findByCategory(category);
	}

	@Autowired
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public Item save(Item item) {
		return itemRepository.save(item);
	}

// 이미지 파일 경로 설정 및 아이템 업데이트
	public void updateItemWithImage(Item item, String imagePath) {
		item.setImagePath(imagePath);
		updateItem(item);
	}

// 이미지 파일 경로 설정 및 아이템 업데이트
	public Item saveItem(Item item) {
		return itemRepository.save(item);
	}

}