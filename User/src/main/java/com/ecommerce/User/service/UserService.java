package com.ecommerce.User.service;
import com.ecommerce.User.DTO.AddressDTO;
import com.ecommerce.User.DTO.UserRequest;
import com.ecommerce.User.DTO.UserResponse;
import com.ecommerce.User.models.Address;
import com.ecommerce.User.models.User;
import com.ecommerce.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final KeyCloakAdminService keyCloakAdminService;

    public List<UserResponse> fetchAllUsers()
    {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse).
                collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest)
    {
        String token = keyCloakAdminService.getAdminAccessToken();
        String keyCloakUserId = keyCloakAdminService.createUser(token,userRequest);
        User user = new User();
        updateUserFromRequest(user , userRequest);
        user.setKeycloakId(keyCloakUserId);
        keyCloakAdminService.assignRealmRoleToUser(userRequest.getUsername(),"USER",keyCloakUserId);
        userRepository.save(user);
    }



    public Optional<UserResponse> fetchUserById(Long id) {

        return userRepository.findById(id).
                map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id , UserRequest updatedUserRequest) {

        Optional<User> existingUser = userRepository.findById(id);
            if(existingUser.isPresent())
            {
                User user = existingUser.get();
                updateUserFromRequest(user,updatedUserRequest);
                userRepository.save(user);
                return true;
            }
        return false;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if(userRequest.getAddress() != null)
        {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user)
    {
        UserResponse userResponse = new UserResponse();
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setUserRole(user.getRole());

        if(user.getAddress() != null)
        {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddressDTO(addressDTO);
        }

        return userResponse;
    }
}
