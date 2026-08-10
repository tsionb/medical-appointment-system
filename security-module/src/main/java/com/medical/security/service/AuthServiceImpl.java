package com.medical.security.service;

import com.medical.common.enums.Role;
import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.department.repository.DepartmentRepository;
import com.medical.doctor.entity.Doctor;
import com.medical.doctor.repository.DoctorRepository;
import com.medical.patient.entity.Patient;
import com.medical.patient.repository.PatientRepository;
import com.medical.security.dto.request.LoginRequest;
import com.medical.security.dto.request.RegisterRequest;
import com.medical.security.dto.response.AuthResponse;
import com.medical.security.entity.User;
import com.medical.security.jwt.JwtUtil;
import com.medical.security.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PatientRepository patientRepository,
                           DoctorRepository doctorRepository,
                           DepartmentRepository departmentRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email '" + request.getEmail() + "' is already registered"
            );
        }

        User user = new User();
        user.setEmail(request.getEmail());


        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        Long profileId = null;

        if (request.getRole() == Role.PATIENT) {

            if (request.getDateOfBirth() == null) {
                throw new IllegalArgumentException(
                        "Date of birth is required for patient registration"
                );
            }
            if (request.getGender() == null || request.getGender().isBlank()) {
                throw new IllegalArgumentException(
                        "Gender is required for patient registration"
                );
            }

            Patient patient = new Patient();
            patient.setFirstName(request.getFirstName());
            patient.setLastName(request.getLastName());
            patient.setEmail(request.getEmail());
            patient.setPhone(request.getPhone());
            patient.setDateOfBirth(request.getDateOfBirth());
            patient.setGender(request.getGender().toUpperCase());
            patient.setAddress(request.getAddress());

            Patient savedPatient = patientRepository.save(patient);
            profileId = savedPatient.getId();

            user.setPatientId(profileId);

        } else if (request.getRole() == Role.DOCTOR) {

            if (request.getSpecialization() == null ||
                request.getSpecialization().isBlank()) {
                throw new IllegalArgumentException(
                        "Specialization is required for doctor registration"
                );
            }
            if (request.getDepartmentId() == null) {
                throw new IllegalArgumentException(
                        "Department ID is required for doctor registration"
                );
            }

            com.medical.department.entity.Department department =
                    departmentRepository.findById(request.getDepartmentId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Department", "id", request.getDepartmentId()));

            Doctor doctor = new Doctor();
            doctor.setFirstName(request.getFirstName());
            doctor.setLastName(request.getLastName());
            doctor.setEmail(request.getEmail());
            doctor.setPhone(request.getPhone());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setDepartment(department);

            Doctor savedDoctor = doctorRepository.save(doctor);
            profileId = savedDoctor.getId();

            user.setDoctorId(profileId);
        }

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(
                savedUser.getEmail(), savedUser.getRole(), profileId);

        return new AuthResponse(
                token,
                savedUser.getRole(),
                savedUser.getId(),
                savedUser.getEmail(),
                profileId
        );
    }


    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {


        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException(
                        "Invalid email or password"
                ));


        if (!user.isEnabled()) {
            throw new BadCredentialsException("Account is disabled");
        }


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }


        Long profileId = null;
        if (user.getRole() == Role.PATIENT) {
            profileId = user.getPatientId();
        } else if (user.getRole() == Role.DOCTOR) {
            profileId = user.getDoctorId();
        }


        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole(), profileId);

        return new AuthResponse(
                token,
                user.getRole(),
                user.getId(),
                user.getEmail(),
                profileId
        );
    }
}
