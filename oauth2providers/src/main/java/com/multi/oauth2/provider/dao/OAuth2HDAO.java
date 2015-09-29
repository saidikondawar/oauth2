package com.multi.oauth2.provider.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.multi.oauth2.provider.dao.bean.ClientBean;
import com.multi.oauth2.provider.dao.bean.TokenBean;
import com.multi.oauth2.provider.dao.bean.UserBean;
import com.multi.oauth2.provider.trans.ClientTrans;
import com.multi.oauth2.provider.trans.TokenTrans;
import com.multi.oauth2.provider.trans.UserTrans;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;

@Transactional
@SuppressWarnings({ "unchecked" })
public class OAuth2HDAO {
	private static final Logger log = LoggerFactory.getLogger(OAuth2HDAO.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public UserVO loginProcess(UserVO vo) throws Exception {

		log.debug("Login Process");
		try {

			UserBean bean = (UserBean) getCurrentSession().get(UserBean.class, vo.getUserid());

			if (bean != null && bean.getPassword().equalsIgnoreCase(vo.getPassword())) {
				UserVO voRe = UserTrans.getBeanFromVO(bean);
				voRe.setPassword(null);
				return voRe;
			}
			return null;

		} catch (RuntimeException re) {
			log.error("login failed", re);
			throw re;
		}
	}

	public UserVO getUserInfo(UserVO vo) throws Exception {

		log.debug("Get User Process");
		try {

			UserBean bean = (UserBean) getCurrentSession().get(UserBean.class, vo.getUserid());

			if (bean != null) {
				UserVO voRe = UserTrans.getBeanFromVO(bean);
				return voRe;
			}
			return null;

		} catch (RuntimeException re) {
			log.error("Get User failed", re);
			throw re;
		}
	}

	public List<ClientVO> getClientList(UserVO vo) throws Exception {

		log.debug("Get Clients Process");
		try {
			List<ClientVO> voList = new ArrayList<ClientVO>();

			Query query = getCurrentSession().createQuery("from ClientVO c where c.userid like :userId");

			query.setParameter("userId", vo.getUserid());

			List<ClientBean> beans = query.list();

			if (beans != null && beans.size() > 0) {

				for (ClientBean bean : beans) {

					voList.add(ClientTrans.getVoToBean(bean));
				}
			}
			return voList;

		} catch (RuntimeException re) {
			log.error("get Clients failed", re);
			throw re;
		}

	}

	public ClientVO getClientOne(ClientVO vo) throws Exception {

		log.debug("Get Client Process");
		try {

			ClientBean bean = (ClientBean) getCurrentSession().get("com.multi.oauth2.provider.dao.bean.ClientBean", vo.getClient_id());

			if (bean != null) {
				ClientVO voRe = ClientTrans.getVoToBean(bean);
				return voRe;
			}
			return null;

		} catch (RuntimeException re) {
			log.error("Get Client failed", re);
			throw re;
		}
	}

	public void deleteClient(ClientVO vo) throws Exception {

		log.debug("deleting IclubAccessType instance");
		try {

			getCurrentSession().delete(getCurrentSession().get(ClientBean.class, vo.getClient_id()));
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void insertClient(ClientVO vo) throws Exception {

		log.debug("saving insertClient instance");
		try {
			getCurrentSession().save(ClientTrans.getBeanToVo(vo));
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}

	}

	public void createToken(TokenVO vo) throws Exception {

		log.debug("saving createToken instance");
		try {
			getCurrentSession().save(TokenTrans.getBeanToVo(vo));
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public TokenVO selectRefreshToken(TokenVO vo) throws Exception {

		log.debug("Get selectRefreshToken Process");
		try {

			Query query = getCurrentSession().createQuery("from TokenVO c where c.refresh_token like :refToken");

			query.setParameter("refToken", vo.getRefresh_token());

			TokenBean bean = (TokenBean) query.uniqueResult();

			if (bean != null) {

				TokenVO voRet = TokenTrans.getBeanToVo(bean);
				return voRet;
			}
			return null;

		} catch (RuntimeException re) {
			log.error("get selectRefreshToken failed", re);
			throw re;
		}
	}

	public TokenVO selectToken(TokenVO vo) throws Exception {

		log.debug("Get selectToken Process");
		try {

			Query query = getCurrentSession().createQuery("from TokenVO c where c.access_token like :accToken");

			query.setParameter("accToken", vo.getAccess_token());

			TokenBean bean = (TokenBean) query.uniqueResult();

			if (bean != null) {

				TokenVO voRet = TokenTrans.getBeanToVo(bean);
				return voRet;
			}
			return null;

		} catch (RuntimeException re) {
			log.error("get selectToken failed", re);
			throw re;
		}
	}

	public TokenVO selectTokenByCode(TokenVO vo) throws Exception {

		log.debug("Get selectTokenByCode Process");
		try {

			Query query = getCurrentSession().createQuery("from TokenVO c where c.code like :code");

			query.setParameter("code", vo.getCode());

			TokenBean bean = (TokenBean) query.uniqueResult();

			if (bean != null) {

				TokenVO voRet = TokenTrans.getBeanToVo(bean);
				return voRet;
			}
			return null;

		} catch (RuntimeException re) {
			log.error("get selectTokenByCode failed", re);
			throw re;
		}
	}

	public void updateAccessToken(TokenVO vo) throws Exception {

		log.debug("merging updateAccessToken instance");
		try {

			Query query = getCurrentSession().createQuery("from TokenVO c where c.refresh_token like :refToken");

			query.setParameter("refToken", vo.getRefresh_token());

			TokenBean bean = (TokenBean) query.uniqueResult();
			bean.getTokenEmb().setAccess_token(vo.getAccess_token());
			bean.setCreated_at(vo.getCreated_at());
			bean = (TokenBean) getCurrentSession().merge(bean);
			log.debug("merge successful");
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}

	}

	public void deleteExpiredToken(TokenVO vo) throws Exception {

		/*
		 * DELETE tbl_token WHERE created_at &lt; GET_TIMESTAMP() - #longval#
		 */

		log.debug("deleteExpiredToken instance");
		try {

			getCurrentSession().delete(getCurrentSession().get(ClientBean.class, vo.getClient_id()));
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}

	}

	public void deleteToken(TokenVO vo) throws Exception {

		log.debug("merging deleteToken instance");
		try {

			Query query = getCurrentSession().createQuery("from TokenVO c where c.access_token like :accToken");

			query.setParameter("accToken", vo.getAccess_token());

			TokenBean bean = (TokenBean) query.uniqueResult();

			getCurrentSession().delete(bean);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void deleteExpiredToken(long ms) throws Exception {

		log.debug("deleteExpiredToken instance");
		try {

			Long curentTime = System.currentTimeMillis();
			Query qurey = getCurrentSession().createSQLQuery("DELETE tbl_token WHERE created_at < :curentTime").setParameter("curentTime", curentTime);

			qurey.executeUpdate();

			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public static OAuth2HDAO getFromApplicationContext(ApplicationContext ctx) {
		return (OAuth2HDAO) ctx.getBean("OAuth2HDAO");
	}

}
