package hieukientung.booktour.controller;

import hieukientung.booktour.model.CartItem;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.UserRepository;
import hieukientung.booktour.service.CartService;
import hieukientung.booktour.service.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    private TourService tourService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("view")
    public String getCartItems(Model model, HttpSession session) {
        List<CartItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        // Calculate the total price
        long totalPrice = cartItems.stream()
                .mapToLong(cartItem -> cartItem.getPrice().longValue() * cartItem.getQuantity())
                .sum();
        model.addAttribute("totalPrice", totalPrice);
        // Calculate cart count
        session.setAttribute("cartCount", cartItems.size());
        //  model.addAttribute("cartCount", cartItems.size());
        return "cart-item";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long tourId) {
        Tour tour = tourService.getTourById(tourId);
        if (tour != null) {
            cartService.addToCart(tour);
        }
        return "redirect:/cart/view";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam("tourId") Long tourId,
                                 @RequestParam("quantity") int quantity) {
        cartService.updateCartItem(tourId, quantity);
        return "redirect:/cart/view";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long tourId) {
        cartService.removeFromCart(tourId);
        return "redirect:/cart/view";
    }

//    @PostMapping("/order")
//    public String Order() {
//        // Get the currently logged-in user
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Process the purchaseProduct product = productService.get(productId);
//        cartService.orderCart(findUser);
//        // Redirect to a success page or return a success message
//        return "/cart/order.html";
//    }

    @PostMapping("/payment")
    public String Payment() {
        // Get the currently logged-in user

        // Redirect to a success page or return a success message
        return "/cart/success";
    }
}