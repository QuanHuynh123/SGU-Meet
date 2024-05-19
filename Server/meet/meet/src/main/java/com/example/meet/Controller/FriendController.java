package com.example.meet.Controller;

import com.example.meet.Model.User;
import com.example.meet.Service.FriendService;
import com.example.meet.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class FriendController {
    @Autowired
    UserService userService;

    @Autowired
    FriendService friendService;
    @GetMapping("/getAddRequest")
    public User getProfileUser(@RequestParam String userId) throws ExecutionException, InterruptedException {
        return userService.getProfileUser(userId);
    }
    @GetMapping("/getFriend")
    public List<User> getFriend(@RequestParam List<String> idFriend) throws ExecutionException, InterruptedException {
        if (idFriend.isEmpty()) {
            // Xử lý trường hợp danh sách idFriend rỗng, ví dụ: trả về một thông báo lỗi
            throw new IllegalArgumentException("Danh sách idFriend không được rỗng");
        } else {
            // Thực hiện hành động khi danh sách idFriend không rỗng
            return friendService.getFriend(idFriend);
        }
    }

    @PostMapping("/addFriend")
    public boolean addFriend(@RequestParam String userId1, @RequestParam String userId2) throws ExecutionException, InterruptedException {
        // Thêm userId1 vào danh sách bạn bè của userId2 và ngược lại
        friendService.addFriend(userId1, userId2);
        friendService.addFriend(userId2, userId1);
        return  true;
    }

    @PostMapping("/requestAddFriend")
    public boolean requestAddFriend(@RequestParam String userId1, @RequestParam String userId2) throws ExecutionException, InterruptedException {
        // Thêm userId1 vào danh sách yêu cầu kết bạn của userId2
        friendService.requestAddFriend(userId1, userId2);
        return  true;
    }

}
