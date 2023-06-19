package hieukientung.booktour.controller;

import hieukientung.booktour.model.CartItem;
import hieukientung.booktour.model.Orders;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.UserRepository;
import hieukientung.booktour.service.CartService;
import hieukientung.booktour.service.OrdersService;
import hieukientung.booktour.service.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
        return "/cart/cart-item";
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

    @PostMapping("/remove/{tourId}")
    public String removeFromCart(@PathVariable("tourId") Long tourId) {
        cartService.removeFromCart(tourId);
        return "redirect:/tour/view-list-tours";
    }

    @PostMapping("/order")
    public String Order() {
        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        hieukientung.booktour.model.User findUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        // Process the purchaseProduct product = productService.get(productId);
        cartService.orderCart(findUser);
        // Redirect to a success page or return a success message
        return "/cart/order";
    }

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/view-payment")
    public String ShowOrdersList(Model model) {
        List<Orders> orders = ordersService.getAllOrders();
        model.addAttribute("orders", orders);
        return "cart/view-payment";
    }

    @PostMapping("/payment/{id}")
    public String Payment(@PathVariable("id") Long orderId) {
        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        hieukientung.booktour.model.User findUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        // Find the orderId to set isPaid
        Orders find_OrderId = ordersService.getOrdersById(orderId);
        if (find_OrderId != null) {
            find_OrderId.setIsPaid(true);
            ordersService.saveOrder(find_OrderId);
        }

        // Redirect to a view-payment page or return a success message
        return "redirect:/cart/view-payment";
    }
}