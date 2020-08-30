package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ImageClass;

import java.util.List;

public interface ImageBO {

    List<ImageClass> findAll();
    ImageClass findById(Long id);
    ImageClass findByCaption(String caption);
    void save(ImageClass image);
    void saveAll(List<ImageClass> images);
    void saveAll(ImageClass... images);
    void deleteById(Long id);
    void delete(ImageClass image);
    Long count();

}