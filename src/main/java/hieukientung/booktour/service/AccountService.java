package hieukientung.booktour.service;

import hieukientung.booktour.model.User;

public interface AccountService {
    User getByUsername(String username);

    void saveUser(User user);
}
