package it.univaq.disim.mwt.postd.business;

import it.univaq.disim.mwt.postd.domain.UserChannelRole;

import java.util.List;

public interface UserChannelRoleBO {

    List<UserChannelRole> findAll();
    List<UserChannelRole> findByChannelId(Long channelId);
    List<UserChannelRole> findByUserId(Long userId);
    Page<UserChannelRole> findByChannelIdPaginated(Long channelId, int page, int size);
    List<UserChannelRole> findByChannelIdAndRoleId(Long channelId, Long roleId);
    UserChannelRole findByChannelIdAndUserId(Long channelId, Long userId);
    void save(UserChannelRole userChannelRole);
    void saveAll(List<UserChannelRole> userChannelRoles);
    void saveAll(UserChannelRole... userChannelRoles);
    void delete(UserChannelRole userChannelRole);
    Long count();

}
