package com.shoppingmall.review;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingmall.item.Item;
import com.shoppingmall.item.ItemService;
import com.shoppingmall.users.UserService;
import com.shoppingmall.users.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@Validated
//@RestController
//@RequestMapping("/reviews")
public class ReviewController {

  @Autowired
  private ReviewService reviewService; 
  @Autowired
  private UserService userService;
  @Autowired
  private ItemService itemService;

	@Autowired
	private final ReviewRepository reviewRepository;

  public ReviewController(ReviewService reviewService,UserService userService,ItemService itemService,ReviewRepository reviewRepository) {
          this.reviewService = reviewService;
          this.userService =userService;
          this.itemService =itemService;
          this.reviewRepository =reviewRepository;
      }


  @PostMapping("/addReview")//원본2//사진추가////스트링 버젼
  public String addReview(Model model,
                          @RequestParam(name = "itemid") String itemid,
                          @RequestParam(name = "contents") String contents,
                          @RequestParam("reviewImage") MultipartFile file,
                          RedirectAttributes attributes,
                          HttpSession session,
                          HttpServletRequest request,
                          HttpServletResponse response) {

      Users loggedInUser = (Users) session.getAttribute("loggedInUser");
      model.addAttribute("loggedInUser", loggedInUser);

      if (loggedInUser == null) {
          // User is not logged in, redirect to the login page
          return "redirect:/login";
      }

      if (contents.isEmpty() || file.isEmpty()) {
          // Content or image not provided, show error message and return to the previous page
          attributes.addFlashAttribute("error", "Please provide both content and an image.");
          return "redirect:" + request.getHeader("Referer");
      }

      try {
          byte[] reviewImageBytes = file.getBytes();
          // Add a review and set the session attribute for itemid
          Review newReview = reviewService.addReview(loggedInUser, Long.parseLong(itemid), contents, session, reviewImageBytes);
          session.setAttribute("itemid", itemid);
          System.out.println("저장할 객체 정보:" + newReview);

      } catch (NumberFormatException e) {
          // Handle the NumberFormatException if the 'itemid' is not a valid long.
          // For example, log the error or return an error response.
          e.printStackTrace();
      } catch (Exception e) {
          // Handle other exceptions if needed
          e.printStackTrace();
      }

      // Set the referrer attribute for redirecting back to the referring page
      String referrer = request.getHeader("Referer");
      session.setAttribute("referrer", referrer);

      // Check if the referrer URL is not empty
      if (referrer != null && !referrer.isEmpty()) {
          // Redirect to the referrer URL
          try {
              response.sendRedirect(referrer);
          } catch (IOException e) {
              // Handle the IOException if needed
              e.printStackTrace();
          }
      }

      // Return a view name or update this part according to your logic
      return "item/itemdetail :: #reviewListContainer";
  }

  @GetMapping("/reviews") //다보이는페이지//admin만 입장가능
  public String getAllReviews(Model model, HttpSession session) {
      Users loggedInUser = (Users) session.getAttribute("loggedInUser");

      // Check if the user is logged in and has the correct email
      if (loggedInUser != null && "admin@admin.com".equals(loggedInUser.getEmail())) {
          // Your existing logic for retrieving reviews
          List<Review> reviews = reviewService.getAllReviews();
          model.addAttribute("reviews", reviews);

          // Additional logic if needed
          return "item/itemdetail :: #reviewListContainer";
      } else {
          // Redirect to a page or show an error message for unauthorized access
          return "redirect:/access-denied"; // You can customize this URL
      }
  }

  @GetMapping("/reviews/{itemid}") // 리뷰 아이템으로 나누기//디스플레이 번호 자동생성//사진
  public String getReviewsByItemId(@PathVariable Long itemid, Model model, HttpSession session, MultipartFile file) {
      Optional<Item> item = itemService.findItemById(itemid);
      List<Review> reviews = reviewService.getReviewsByItem(item);
      reviewService.updateDisplayNumbersByItemId(item);
      
   // 컨트롤러에서 리뷰 리스트에 번호 추가하기
      for (int i = 0; i < reviews.size(); i++) {
          reviews.get(i).setDisplayNumber(i + 1);
      }

      
      model.addAttribute("reviews", reviews);
      Users loggedInUser = (Users) session.getAttribute("loggedInUser");
      model.addAttribute("loggedInUser", loggedInUser);
      session.setAttribute("usersid", loggedInUser);
      return "item/itemdetail :: #reviewListContainer";
  }


  @GetMapping("/getImage/{rvid}")//이미지 가져오기 //원페이지 이미지만뜸
  @ResponseBody
  public ResponseEntity<byte[]> getImage(@PathVariable Long rvid) {
	  Optional<Review> optionalReview = reviewRepository.findById(rvid);

      if (optionalReview.isPresent()) {
          Review review = optionalReview.get();
          byte[] imageBytes = review.getReviewImage();
          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.IMAGE_PNG);
          headers.setContentType(MediaType.IMAGE_GIF);
          headers.setContentType(MediaType.IMAGE_JPEG); // 이미지 타입에 따라 변경
          return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
      } else {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
  }

  

@GetMapping("/updateReview/{rvid}")// 원본//기존사진//원페이지리뷰가 없다고뜸
public String showUpdateReviewForm(@PathVariable Long rvid, Model model, HttpSession session, MultipartFile file) {
   Review review = reviewService.getReviewById(rvid);
   if (review == null) {
		return "error"; // 에러 페이지로 리다이렉트 또는 처리
	}
   model.addAttribute("review", review);
   
   Users loggedInUser = (Users) session.getAttribute("loggedInUser");
   session.setAttribute("usersid", loggedInUser);
   model.addAttribute("loggedInUser", loggedInUser);
   
  System.out.println(review);
   return "item/itemdetail :: #reviewListContainer"; // 현재 페이지의 이름 (itemdetail.html 등)
}



@PostMapping("/updateReview")//기존이미지 가져오기//원페이지 안뜸
public String updateReview(
        HttpSession session,
        @RequestParam Long rvid,
        @RequestParam Item itemid,
        @RequestParam Users usersid,
        @ModelAttribute("review") Review review,
        Model model,
        @RequestHeader(value = "referer", defaultValue = "/") String referer) {

    // 여기서 rvid와 review 객체에는 이미 값이 포함되어 있어야 합니다.
    review.setRvid(rvid); // 필요하다면 rvid를 Review 객체에 설정해주는 부분
    review.setItem(itemid); // 아이템 아이디 설정
    review.setUsers(usersid); // 유저 아이디 설정

    // 리뷰 업데이트 시에 새로운 이미지가 전송되지 않았다면 기존 이미지를 유지하도록 처리
    Review existingReview = reviewService.getReviewById(rvid);
    if (existingReview != null && review.getReviewImage() == null) {
        review.setReviewImage(existingReview.getReviewImage());
    }

    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    model.addAttribute("loggedInUser", loggedInUser);
    session.setAttribute("usersid", loggedInUser);

    reviewService.updateReview(review);
    return "redirect:" + referer;
}



@GetMapping("/deleteReview/{rvid}")
public String deleteReview(Model model,
@PathVariable Long rvid,
 HttpSession session,
@RequestHeader(value = "referer", defaultValue = "/") String referer
) {
reviewService.deleteReview(rvid);
Users loggedInUser = (Users) session.getAttribute("loggedInUser");
model.addAttribute("loggedInUser", loggedInUser);
session.setAttribute("usersid", loggedInUser);

// Referer가 없을 경우 기본값으로 "/" (홈 페이지)으로 리다이렉트
return "redirect:" + referer;
}



@PostMapping("/likeReview/{rvid}")
public String likeReview(@PathVariable Long rvid, HttpSession session) {
  Users loggedInUser = (Users) session.getAttribute("loggedInUser");

  if (loggedInUser != null) {
      // 사용자가 로그인한 경우에만 처리

      Review review = reviewService.getReviewById(rvid);
      if (review != null) {
          // 리뷰가 존재하는 경우에만 처리
          review.setLikes(review.getLikes() + 1);
          reviewService.saveReview(review);
      }
  }

  // 리다이렉트 또는 다른 응답 처리
  return "redirect:/reviews"; // 또는 적절한 리다이렉트 URL
}




@PostMapping("/updateLikes")
public ResponseEntity<String> updateLikes(@RequestParam Long rvid) {
    // Perform the necessary logic to update likes in the server
    // ...

    // You can return a success message or status
    return ResponseEntity.ok("Likes updated successfully");
}




}