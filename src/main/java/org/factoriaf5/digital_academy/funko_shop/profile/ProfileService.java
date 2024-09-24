package org.factoriaf5.digital_academy.funko_shop.profile;

import org.factoriaf5.digital_academy.funko_shop.profile.profile_exceptions.ProfileNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.factoriaf5.digital_academy.funko_shop.user.UserRepository;
import org.factoriaf5.digital_academy.funko_shop.user.user_exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private UserRepository userRepository;

    public void createProfile(Long userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Profile profile = new Profile();
        profile.setUser(user);

        profileRepository.save(profile);
    }

    public List<ProfileDTO> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProfileDTO getProfileByUser(User user) {
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        return mapToDTO(profile);
    }

    public ProfileDTO updateProfile(Long id, ProfileDTO profileDTO) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        existingProfile.setFirstName(profileDTO.getFirstName());
        existingProfile.setLastName(profileDTO.getLastName());
        existingProfile.setPhoneNumber(profileDTO.getPhoneNumber());
        existingProfile.setStreet(profileDTO.getStreet());
        existingProfile.setCity(profileDTO.getCity());
        existingProfile.setRegion(profileDTO.getRegion());
        existingProfile.setPostalCode(profileDTO.getPostalCode());
        existingProfile.setCountry(profileDTO.getCountry());
        existingProfile.isShipping();
        existingProfile.isSubscribed();

        Profile updatedProfile = profileRepository.save(existingProfile);
        return mapToDTO(updatedProfile);
    }

    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ProfileNotFoundException("Profile not found");
        }
        profileRepository.deleteById(id);

    }

    ProfileDTO mapToDTO(Profile profile) {

        return new ProfileDTO(
                profile.getId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhoneNumber(),
                profile.getStreet(),
                profile.getCity(),
                profile.getRegion(),
                profile.getPostalCode(),
                profile.getCountry(),
                profile.isShipping(),
                profile.isSubscribed(),
                profile.getUser().getId(), 
                null);

    }
}