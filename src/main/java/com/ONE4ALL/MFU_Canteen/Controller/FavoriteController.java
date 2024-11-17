package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ONE4ALL.MFU_Canteen.Entity.FavoriteItem;
import com.ONE4ALL.MFU_Canteen.Service.FavoriteService;

@Controller
@RequestMapping("/user/{userId}")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;
    
    @GetMapping("/favorite")
    public String showFavoriteItems(@PathVariable Long userId, Model model){
        List<FavoriteItem> favoriteItems = favoriteService.getFavoritesByUser(userId);
        model.addAttribute("favoriteItems", favoriteItems);

        return "favorite";
    }

    @PostMapping("/favorite/toggle")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleFavorite(@PathVariable Long userId, @RequestParam Long itemId) {
        Map<String, Object> response = new HashMap<>();
        try {
            favoriteService.toggleFavorite(userId, itemId);
            boolean isFavorite = favoriteService.isItemFavorite(userId, itemId);
            response.put("message", isFavorite ? "Item added to favorites" : "Item removed from favorites");
            response.put("isFavorite", isFavorite); // Add this
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to update favorite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }    

}
