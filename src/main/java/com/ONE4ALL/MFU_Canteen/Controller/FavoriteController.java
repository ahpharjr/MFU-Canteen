package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.List;

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
    public ResponseEntity<String> toggleFavorite(@PathVariable Long userId, @RequestParam Long itemId){
        try{
            favoriteService.toggleFavorite(userId, itemId);
            return ResponseEntity.ok("Favorite toggle successfully.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to toggle favorite");
        }
    }
}
