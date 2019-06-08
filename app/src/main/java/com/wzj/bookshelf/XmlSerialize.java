package com.wzj.bookshelf;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlSerialize {

    public void write_xml(Context context, List<Book> bookList, File file) {

        try {
            //创建Docunment工厂
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            //创建Doucumentbuilder
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            //实例化一个xml文件
            Document newxml = builder.newDocument();
            //创建根标签
            Element book_list = newxml.createElement("BookList");

            for(int i =0 ; i<3;i++)
            {
                //创建BookList子标签
//                Byte[] picturebyte = new Byte[211];
//                final InputStream inputStream = context.getAssets().open("111.png");
//                inputStream.read(picturebyte,0,211);

                Element book = newxml.createElement("book");
                book.setAttribute("id","1");

                //创建book子标签
                Element cover = newxml.createElement("cover");
                cover.setTextContent("R.drawable.ic_menu_camera");

                Element name = newxml.createElement("name");
                name.setTextContent("name"+i);

                Element introduct = newxml.createElement("introduct");
                introduct.setTextContent("introduct"+i);

                //将cover和name和introduct 放入book的标签中
                book.appendChild(cover);
                book.appendChild(name);
                book.appendChild(introduct);
                //将book放进BookList中
                book_list.appendChild(book);
            }
            //加入xml文件中
            newxml.appendChild(book_list);
            //实例化Transformer工厂
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            //获取到Transformer
            Transformer transformer = transformerFactory.newTransformer();
            //设置输出格式
            transformer.setOutputProperty("encoding","UTF-8");
            //设置输出流
            OutputStream os = new FileOutputStream(file);
            //将文件写出
            transformer.transform(new DOMSource(newxml),new StreamResult(os));

            Toast.makeText(context,"生成成功",Toast.LENGTH_LONG).show();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }


    public List<Book> read_xml (Context context, File file)
    {
        List<Book>  bookList = new ArrayList<Book>();
        try {
            //InputStream inputStream  = context.getAssets().open(filename+".xml");
            //获取DOM解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //获取DOM解析器
            DocumentBuilder builder = factory.newDocumentBuilder();
            //将解析树放入内存，通过返回值Docunment来描述结果
            Document document = builder.parse(file);
            //获取根元素
            Element root = document.getDocumentElement();
            //取得所有book节点集合
            NodeList bookNodes = root.getElementsByTagName("book");
            for (int i = 0; i < bookNodes.getLength(); i++) {
                Book book = new Book();
                //取得Book节点元素
                Element bookElement = (Element) bookNodes.item(i);
                //取得属性
                book.setId(Integer.parseInt(bookElement.getAttribute("id")));
                //获取子节点
                NodeList bookChilds = bookElement.getChildNodes();
                for (int j = 0; j < bookChilds.getLength(); j++) {
                    //判断当前节点是否为元素类型的节点
                    if (bookChilds.item(j).getNodeType() == Node.ELEMENT_NODE) {

//                        book.setCover(Integer.parseInt(bookElement.getElementsByTagName("cover").item(0).getTextContent()));

                        book.setName(bookElement.getElementsByTagName("name").item(0).getTextContent());

                        book.setIntroduct(bookElement.getElementsByTagName("introduct").item(0).getTextContent());

                    }
                }
                bookList.add(book);
            }
            Toast.makeText(context.getApplicationContext(), "读出成功", Toast.LENGTH_LONG).show();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookList;
    }
















}
//    public boolean saveObject(Context context, Serializable ser, String name) {
//        FileOutputStream fos = null;
//        ObjectOutputStream oos = null;
//        try {
//            fos = context.openFileOutput(""+name+".txt", Context.MODE_PRIVATE);
//            oos = new ObjectOutputStream(fos);
//            oos.writeObject(ser);
//            oos.flush();
//            return true;
//        } catch ( Exception e ) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                oos.close();
//            } catch ( Exception e ) {
//                e.printStackTrace();
//            }
//            try {
//                fos.close();
//            } catch ( Exception e ) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public Serializable readObject(Context context,String name) {
//        FileInputStream fis = null;
//        ObjectInputStream ois = null;
//        try {
//            fis = context.openFileInput(""+name+".txt");
//            ois = new ObjectInputStream(fis);
//            return (Serializable) ois.readObject();
//
//        } catch ( FileNotFoundException e ) {
//        } catch ( Exception e ) {
//            e.printStackTrace();
//// 反序列化失败 - 删除缓存文件
//            if ( e instanceof InvalidClassException) {
//                File data = context.getFileStreamPath(""+name+".txt");
//                data.delete();
//            }
//        } finally {
//            try {
//                ois.close();
//            } catch ( Exception e ) {
//            }
//            try {
//                fis.close();
//            } catch ( Exception e ) {
//            }
//        }
//        return null;
//    }

