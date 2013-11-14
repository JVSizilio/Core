package br.com.ats.hibernate;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe abstrata que implementa os métodos básicos de acesso ao banco de dados
 * baseado no session factory
 */
public abstract class AbstractHibernateDAO<T> extends HibernateExceptionTranslator  {
    private Class<T> entityClass;
    @Autowired
    SessionFactory sessionFactory;
    
    /**
     * Construtora. Pega por reflexão o tipo do template fornecido
     */
    public AbstractHibernateDAO() {
        if (((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].toString().compareTo("T") != 0) {
            this.entityClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
    }

    /**
     * Função utilizada apenas para reduzir o códgo das outras funções reduzindo
     * o texto necessário ser escrito para recuperar a sessão. Depois será
     * utilizada para tratar problemas na conexão Também permite que as classes
     * filhas tenham acesso a sessão.
     *
     * @return Session - Sessão recuperar da sessionFactory
     */
    protected final Session getCurrentSession() {
        try {
            Session session = sessionFactory.getCurrentSession();

            return session;
        } catch (Exception e) {
            //throw e;
            //e.printStackTrace();
            return null; //TODO: Tratar o problema da sesssão de forma mais eficiente
        }
    }

    /**
     * Gera um object Criteria do objecto especificado no template
     *
     * @return Criteria Objecto criteria do Hibernate para a entidade
     * especificada no template
     */
    protected Criteria getCriteria() {
        return this.getCurrentSession().createCriteria(entityClass);
    }

    /**
     * Retorna um valor indicando se o objeto no DAO possui ou não a propriedade
     * status
     *
     * @return Integer 0 se não possuir, 1 se possuir e for do tipo Sort e 2 se
     * possuir e for do tipo Integer
     */
    protected Integer hasStatus() {
        try {
            Boolean hasStatus = false;
            Boolean isShort = false;
            Method method;

            try {
                method = entityClass.getDeclaredMethod("setStatus", Short.class);
                hasStatus = true;
                isShort = true;
            } catch (NoSuchMethodException nsme) {
                method = null;
            }

            if (method == null) {
                try {
                    method = entityClass.getDeclaredMethod("setStatus", Integer.class);
                    hasStatus = true;
                } catch (NoSuchMethodException nsme) {
                    method = null;
                }
            }

            if (hasStatus && isShort) {
                return 1;
            } else if (hasStatus && !isShort) {
                return 2;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retorna se o objeto possui a propriedade nome
     *
     * @return Boolean true se possuir e false se não possuir
     */
    protected Boolean hasNome() {
        try {
            Method method;

            try {
                method = entityClass.getDeclaredMethod("setNome", String.class);
                return true;
            } catch (NoSuchMethodException nsme) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Recupera um objeto do banco baseado no id fornecedido.
     *
     * @param id - Inteiro que identifca o objeto pela chave primaria a ser
     * recuperado no banco
     * @return Object do tipo fornecido no template
     */
    public T open(final Integer id) {
        try {
            return (T) getCurrentSession().get(entityClass, id);
        } catch (final HibernateException he) {
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Recupera um objeto do banco baseado no id fornecedido.
     *
     * @param id - Long que identifca o objeto pela chave primaria a ser
     * recuperado no banco
     * @return Object do tipo fornecido no template
     */
    public T open(final Long id) {
        try {
            return (T) getCurrentSession().get(entityClass, id);
        } catch (final HibernateException he) {
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Recupera um objeto do banco baseado no id fornecedido usando a o evict.
     *
     * @param id - Inteiro que identifca o objeto pela chave primaria a ser
     * recuperado no banco
     * @return Object do tipo fornecido no template
     */
    @Transactional(readOnly = true)
    public T openEvict(final Integer id) {
        try {
            Object obj = getCurrentSession().get(entityClass, id);
            getCurrentSession().evict(obj);

            return (T) obj;
        } catch (final HibernateException he) {
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Retorna uma lista contendo todos os dados armazenados no banco de dados.
     *
     * @return Lista de objetos do tipo fornecido no template
     */
    @Transactional(readOnly = true)
    public List< T> listAll() {
        try {
            Criteria cri = getCriteria();
            if (hasNome()) {
                cri.addOrder(org.hibernate.criterion.Order.asc("nome"));
            }

            return cri.list();
        } catch (final HibernateException he) {
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Retorna uma lista contendo apenas os objetos cujo status seja ativo
     *
     * @return List de objeto
     */
    @Transactional(readOnly = true)
    public List< T> listAllOk() {
        try {
            Integer status = hasStatus();
            if (status > 0) {
                Criteria cri = getCriteria();
                if (hasNome()) {
                    cri.addOrder(org.hibernate.criterion.Order.asc("nome"));
                }

                switch (status) {
                    case 1:
                        cri.add(Restrictions.eq("status", Short.parseShort("1")));
                        return cri.list();
                    case 2:
                        cri.add(Restrictions.eq("status", 1));
                        return cri.list();
                    default:
                        return listAll();
                }
            } else {
                return listAll();
            }
        } catch (final HibernateException he) {
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Salva a entidade fornecida no banco de dados
     *
     * @param entity
     * @return
     */
    public T save(T entity) {
        Transaction tx = getCurrentSession().beginTransaction();
        try {
            Integer newId = (Integer) getCurrentSession().save(entity);

            tx.commit();
            getCurrentSession().flush();
            getCurrentSession().clear();
            T t = this.open(newId);

            return t;
        } catch (final HibernateException he) {      
            he.printStackTrace();
            tx.rollback();
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Atualiza uma entidade fornecida no banco de dados
     *
     * @param entity
     * @return
     */
    public T update(final T entity) {
        Transaction tx = getCurrentSession().beginTransaction();
        try {
            getCurrentSession().update(entity);

            tx.commit();
            getCurrentSession().flush();
            getCurrentSession().clear();

            return entity;
        } catch (final HibernateException he) {
            he.printStackTrace();
            tx.rollback();
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Remove a entidade fornecida do banco de dados
     *
     * @param entity Objeto que deseja deletar
     * @return
     */
    public boolean delete(final T entity) {
        Transaction tx = getCurrentSession().beginTransaction();
        try {
            getCurrentSession().delete(entity);

            tx.commit();
            getCurrentSession().flush();
            getCurrentSession().clear();

            return true;
        } catch (final HibernateException he) {
            he.printStackTrace();
            tx.rollback();
            throw convertHibernateAccessException(he);
        }
    }

    /**
     * Remove a entidade que possui o id fornecido na função
     *
     * @param entityId
     * @return
     */
    public boolean deleteById(final Integer entityId) {
        try {
            final T entity = open(entityId);
            delete(entity);

            return true;
        } catch (final HibernateException he) {
            throw convertHibernateAccessException(he);
        }
    }
}
