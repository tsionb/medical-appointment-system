package com.medical.doctor.service;

import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.doctor.dto.request.CreateDoctorRequest;
import com.medical.doctor.dto.request.UpdateDoctorRequest;
import com.medical.doctor.dto.response.DoctorResponse;
import com.medical.doctor.entity.Doctor;
import com.medical.doctor.repository.DoctorRepository;
import com.medical.department.entity.Department;
import com.medical.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final DepartmentRepository departmentRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }


    @Override
    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

        // Rule 1: email must be unique across all doctors
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                "Doctor with email '" + request.getEmail() + "' already exists"
            );
        }


        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Department", "id", request.getDepartmentId()));

        // Map request DTO → entity
        Doctor doctor = new Doctor();
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setDepartment(department);

        Doctor saved = doctorRepository.save(doctor);

        return DoctorResponse.fromEntity(saved, null);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> {
                    Double avgRating = doctorRepository
                            .findAverageRatingByDoctorId(doctor.getId());
                    return DoctorResponse.fromEntity(doctor, avgRating);
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByDepartment(Long departmentId) {
        // Verify department exists first
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        return doctorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(doctor -> {
                    Double avgRating = doctorRepository
                            .findAverageRatingByDoctorId(doctor.getId());
                    return DoctorResponse.fromEntity(doctor, avgRating);
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization)
                .stream()
                .map(doctor -> {
                    Double avgRating = doctorRepository
                            .findAverageRatingByDoctorId(doctor.getId());
                    return DoctorResponse.fromEntity(doctor, avgRating);
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        Double avgRating = doctorRepository.findAverageRatingByDoctorId(id);
        return DoctorResponse.fromEntity(doctor, avgRating);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, UpdateDoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            doctor.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            doctor.setLastName(request.getLastName());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            boolean emailChanged = !request.getEmail().equals(doctor.getEmail());
            if (emailChanged && doctorRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException(
                    "Email '" + request.getEmail() + "' is already registered"
                );
            }
            doctor.setEmail(request.getEmail());
        }

        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            doctor.setPhone(request.getPhone());
        }

        if (request.getSpecialization() != null && !request.getSpecialization().isBlank()) {
            doctor.setSpecialization(request.getSpecialization());
        }


        if (request.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Department", "id", request.getDepartmentId()));
            doctor.setDepartment(department);
        }

        Doctor updated = doctorRepository.save(doctor);
        Double avgRating = doctorRepository.findAverageRatingByDoctorId(id);
        return DoctorResponse.fromEntity(updated, avgRating);
    }

  
    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor", "id", id);
        }
        doctorRepository.deleteById(id);
    }

    
    @Override
    @Transactional(readOnly = true)
    public Doctor getDoctorEntityById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
    }
}
